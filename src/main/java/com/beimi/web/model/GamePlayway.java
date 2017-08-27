package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_game_playway")
@org.hibernate.annotations.Proxy(lazy = false)
public class GamePlayway implements java.io.Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8988042477190235024L;
	private String id ;
	private String name ;
	private String code ;
	private Date createtime ;
	private String parentid ;
	private String typeid ;
	private String creater;
	private String username ;
	
	private String status ;	//当前状态
	
	private int score;		//底分
	private int mincoins ;	//最小金币数量
	private int maxcoins ;	//最大金币数量
	
	private int cardsnum ;	//每个玩家获牌数量
	
	private boolean changecard ;	//换牌
	
	private boolean shuffle ;	//是否洗牌
	
	
	private Date updatetime ;
	private String orgi ;
	private String area ;
	
	private String game ;	//游戏类型 ： 麻将：地主：德州
	private int players ;	//游戏人数
	
	private int numofgames ;//局数 ， 大厅游戏未 0 表示 无限
	
	
	private String roomtype ;	//房间类型， 房卡：大厅
	
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public int getPlayers() {
		return players;
	}
	public void setPlayers(int players) {
		this.players = players;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getNumofgames() {
		return numofgames;
	}
	public void setNumofgames(int numofgames) {
		this.numofgames = numofgames;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getMincoins() {
		return mincoins;
	}
	public void setMincoins(int mincoins) {
		this.mincoins = mincoins;
	}
	public int getMaxcoins() {
		return maxcoins;
	}
	public void setMaxcoins(int maxcoins) {
		this.maxcoins = maxcoins;
	}
	public boolean isChangecard() {
		return changecard;
	}
	public void setChangecard(boolean changecard) {
		this.changecard = changecard;
	}
	public boolean isShuffle() {
		return shuffle;
	}
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}
	public int getCardsnum() {
		return cardsnum;
	}
	public void setCardsnum(int cardsnum) {
		this.cardsnum = cardsnum;
	}
}
