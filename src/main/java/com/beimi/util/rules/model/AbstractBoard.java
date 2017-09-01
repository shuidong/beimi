package com.beimi.util.rules.model;

public abstract class AbstractBoard implements java.io.Serializable {
	
	/**
	 * 翻底牌，每一种游戏的实现方式不同
	 * @return
	 */
	public abstract byte[] pollLastHands() ;
	
	/**
	 * 计算倍率， 规则每种游戏不同，斗地主 翻到底牌 大小王 翻倍
	 * @return
	 */
	public abstract int calcRatio() ;
	
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private byte[] cards;	//4个Bit描述一张牌，麻将：136+2/2 = 69 byte ; 扑克 54/2 = 27 byte 
	private Player[] players;//3~10人(4 byte)
	
	private TakeCards current;
	
	private String room ;		//房间ID（4 byte）
	
	private byte[] lasthands ;	//底牌
	
	private byte position ;		//地主牌
	
	private boolean docatch ;	//叫地主 OR 抢地主
	private int ratio = 1;			//倍数
	
	private String banker ;		//庄家|地主
	private String currplayer ;	//当前出牌人
	private byte currcard ;		//当前出牌
	
	/**
	 * 找到玩家数据
	 * @param userid
	 * @return
	 */
	public Player player(String userid){
		Player temp = null;
		if(this.players!=null){
			for(Player user : players){
				if(user.getPlayuser()!=null && user.getPlayuser().equals(userid)){
					temp = user ; break ;
				}
			}
		}
		return temp ;
	}
	
	public byte[] getCards() {
		return cards;
	}
	public void setCards(byte[] cards) {
		this.cards = cards;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBanker() {
		return banker;
	}
	public void setBanker(String banker) {
		this.banker = banker;
	}
	public String getCurrplayer() {
		return currplayer;
	}
	public void setCurrplayer(String currplayer) {
		this.currplayer = currplayer;
	}
	public byte getCurrcard() {
		return currcard;
	}
	public void setCurrcard(byte currcard) {
		this.currcard = currcard;
	}
	public byte getPosition() {
		return position;
	}
	public void setPosition(byte position) {
		this.position = position;
	}
	public boolean isDocatch() {
		return docatch;
	}
	public void setDocatch(boolean docatch) {
		this.docatch = docatch;
	}
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public byte[] getLasthands() {
		return lasthands;
	}

	public void setLasthands(byte[] lasthands) {
		this.lasthands = lasthands;
	}

	public TakeCards getCurrent() {
		return current;
	}

	public void setCurrent(TakeCards current) {
		this.current = current;
	}
}
