package com.beimi.web.handler.apps.business.platform;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.PlayUser;
import com.beimi.web.service.repository.es.PlayUserESRepository;

@Controller
@RequestMapping("/apps/platform")
public class GameUsersController extends Handler{
	
	@Autowired
	private PlayUserESRepository playersRes ;
	
	@RequestMapping({"/gameusers"})
	@Menu(type="platform", subtype="gameusers")
	public ModelAndView gameusers(ModelMap map , HttpServletRequest request , @Valid String id){
		Page<PlayUser> playerList = playersRes.findByOrgi(super.getOrgi(request), new PageRequest(super.getP(request), super.getPs(request))) ;
		for(PlayUser player  : playerList.getContent()){
			if(CacheHelper.getGamePlayerCacheBean().getCacheObject(player.getId(), super.getOrgi(request))!=null){
				player.setOnline(true);
			}
		}
		map.addAttribute("playersList", playerList) ;
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/data/index"));
	}
	
	@RequestMapping({"/gameusers/edit"})
	@Menu(type="platform", subtype="gameusers")
	public ModelAndView edit(ModelMap map , HttpServletRequest request , @Valid String id){
		
		map.addAttribute("playUser", playersRes.findById(id)) ;
		
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/data/edit"));
	}
	
	@RequestMapping("/gameusers/update")
    @Menu(type = "admin" , subtype = "gameusers")
    public ModelAndView update(HttpServletRequest request ,@Valid PlayUser players) {
		PlayUser playUser = playersRes.findById(players.getId()) ;
		if(playUser!=null){
			playUser.setDisabled(players.isDisabled());
			playUser.setGoldcoins(players.getGoldcoins());
			playUser.setCards(players.getCards());
			playUser.setDiamonds(players.getDiamonds());
			playUser.setUpdatetime(new Date());
		}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/platform/gameusers.html"));
    }
}
