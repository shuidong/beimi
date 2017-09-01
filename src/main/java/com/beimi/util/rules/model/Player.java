package com.beimi.util.rules.model;

public class Player implements java.io.Serializable , Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public Player(String id){
		this.playuser = id ;
		
	}
	private String playuser ;	//userid对应
	private byte[] cards ;	//玩家手牌，顺序存储 ， 快速排序（4个Bit描述一张牌，玩家手牌 麻将 13+1/2 = 7 byte~=long）
	private byte[] history ;//出牌历史 ， 特权可看
	private byte info ;		//复合信息存储，用于存储玩家位置（2^4,占用4个Bit，最大支持16个玩家）（是否在线1个Bit），是否庄家/地主（1个Bit），是否当前出牌玩家（1个Bit）（是否机器人1个Bit）
	private boolean randomcard ;	//起到地主牌的人
	private boolean docatch ;	//抢过庄（地主）
	private boolean recatch ;	//补抢
	private boolean accept ;	//抢地主 : 过地主
	private byte[] played ;	//杠碰吃胡

	public byte[] getCards() {
		return cards;
	}

	public void setCards(byte[] cards) {
		this.cards = cards;
	}

	public byte getInfo() {
		return info;
	}

	public void setInfo(byte info) {
		this.info = info;
	}

	public byte[] getPlayed() {
		return played;
	}

	public void setPlayed(byte[] played) {
		this.played = played;
	}

	public byte[] getHistory() {
		return history;
	}

	public void setHistory(byte[] history) {
		this.history = history;
	}

	public String getPlayuser() {
		return playuser;
	}

	public void setPlayuser(String playuser) {
		this.playuser = playuser;
	}

	public boolean isRandomcard() {
		return randomcard;
	}

	public void setRandomcard(boolean randomcard) {
		this.randomcard = randomcard;
	}

	public boolean isDocatch() {
		return docatch;
	}

	public void setDocatch(boolean docatch) {
		this.docatch = docatch;
	}
	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	@Override
    public Player clone(){
        try {
			return (Player) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
    }

	public boolean isRecatch() {
		return recatch;
	}

	public void setRecatch(boolean recatch) {
		this.recatch = recatch;
	}
	
}
