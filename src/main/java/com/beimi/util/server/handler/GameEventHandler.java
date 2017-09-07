
package com.beimi.util.server.handler;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.beimi.config.web.model.Game;
import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.state.GameEvent;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.client.NettyClients;
import com.beimi.util.rules.model.Board;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.PlayUserClientESRepository;
import com.beimi.web.service.repository.jpa.PlayUserClientRepository;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

public class GameEventHandler     
{  
	protected SocketIOServer server;
	
	private Game game ;
	
    @Autowired  
    public GameEventHandler(SocketIOServer server , Game game)   
    {  
        this.server = server ;
        this.game = game ;
    }  
    
    @OnConnect  
    public void onConnect(SocketIOClient client)  
    {  
    }  
    
  //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
		if(beiMiClient!=null){
			PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(beiMiClient.getUserid(), beiMiClient.getOrgi()) ;
			/**
			 * 更新当前玩家状态，在线|离线
			 */
			playUser.setOnline(false);
			UKTools.published(playUser,BMDataContext.getContext().getBean(PlayUserClientESRepository.class), BMDataContext.getContext().getBean(PlayUserClientRepository.class));
			BMDataContext.getGameEngine().leaveRoom(playUser, beiMiClient.getOrgi());
			
			NettyClients.getInstance().removeClient(client.getSessionId().toString());
		}
    }  
    
  //抢地主事件
    @OnEvent(value = "joinroom")   
    public void onJoinRoom(SocketIOClient client , AckRequest request, String data)  
    {  
    	BeiMiClient beiMiClient = JSON.parseObject(data , BeiMiClient.class) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			/**
			 * Token不为空，并且，验证Token有效，验证完毕即开始进行游戏撮合，房卡类型的
			 * 1、大厅房间处理
			 *    a、从房间队列里获取最近一条房间信息
			 *    b、将token对应玩家加入到房间
			 *    c、如果房间凑齐了玩家，则将房间从等待撮合队列中移除，放置到游戏中的房间信息，如果未凑齐玩家，继续扔到队列
			 *    d、通知房间的所有人，有新玩家加入
			 *    e、超时处理，增加AI进入房价
			 *    f、事件驱动
			 *    g、定时器处理
			 * 2、房卡房间处理
			 * 	  a、创建房间
			 * 	  b、加入到等待中队列
			 */
			Token userToken ;
			if(beiMiClient!=null && !StringUtils.isBlank(token) && (userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, beiMiClient.getOrgi()))!=null){
				//鉴权完毕
				PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				beiMiClient.setClient(client);
				beiMiClient.setUserid(userClient.getId());
				beiMiClient.setSession(client.getSessionId().toString());
				NettyClients.getInstance().putClient(userClient.getId(), beiMiClient);
				
				/**
				 * 更新当前玩家状态，在线|离线
				 */
				userClient.setOnline(true);
				UKTools.published(userClient,BMDataContext.getContext().getBean(PlayUserClientESRepository.class), BMDataContext.getContext().getBean(PlayUserClientRepository.class));
				
				GameEvent gameEvent = BMDataContext.getGameEngine().gameRequest(userToken.getUserid(), beiMiClient.getPlayway(), beiMiClient.getRoom(), beiMiClient.getOrgi(), userClient) ;
				if(gameEvent != null){
					/**
					 * 游戏状态 ， 玩家请求 游戏房间，活动房间状态后，发送事件给 StateMachine，由 StateMachine驱动 游戏状态 ， 此处只负责通知房间内的玩家
					 * 1、有新的玩家加入
					 * 2、给当前新加入的玩家发送房间中所有玩家信息（不包含隐私信息，根据业务需求，修改PlayUserClient的字段，剔除掉隐私信息后发送）
					 */
					ActionTaskUtils.sendEvent("joinroom", UKTools.json(userClient) , gameEvent.getGameRoom());
					client.sendEvent("players", UKTools.json(CacheHelper.getGamePlayerCacheBean().getCacheObject(gameEvent.getRoomid(), beiMiClient.getOrgi())));
					/**
					 * 当前是在游戏中还是 未开始
					 */
					Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameEvent.getRoomid(), gameEvent.getOrgi());
					if(board !=null){
						if(board.isFinished()){
							BMDataContext.getGameEngine().finished(gameEvent.getRoomid(),gameEvent.getOrgi() );
							//game.change(gameEvent);	//通知状态机 , 此处应由状态机处理异步执行
						}else{
							//恢复数据
						}
					}else{
						//通知状态
					}
					game.change(gameEvent);	//通知状态机 , 此处应由状态机处理异步执行
				}
			}
		}
    }
      
    //抢地主事件
    @OnEvent(value = "docatch")   
    public void onCatch(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().actionRequest(roomid, playUser, playUser.getOrgi(), true);
			}
		}
    }
    
    //不抢/叫地主事件
    @OnEvent(value = "giveup")   
    public void onGiveup(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().actionRequest(roomid, playUser, playUser.getOrgi(), false);
			}
		}
    }
    
    
    //出牌
    @OnEvent(value = "doplaycards")   
    public void onPlayCards(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token) && !StringUtils.isBlank(data)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String[] cards = data.split(",") ;
				
				byte[] playCards = new byte[cards.length] ;
				for(int i= 0 ; i<cards.length ; i++){
					playCards[i] = Byte.parseByte(cards[i]) ;
				}
				BMDataContext.getGameEngine().takeCardsRequest(roomid, userToken.getUserid(), userToken.getOrgi() , false , playCards);
			}
		}
    }
    
  //出牌
    @OnEvent(value = "nocards")   
    public void onNoCards(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().takeCardsRequest(roomid, userToken.getUserid(), userToken.getOrgi() , false , null);
			}
		}
    }
}  