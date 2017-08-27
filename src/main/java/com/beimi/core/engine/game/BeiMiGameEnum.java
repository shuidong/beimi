package com.beimi.core.engine.game;

public enum BeiMiGameEnum {
	/**
	 * 游戏的基本状态，开局->等待玩家（AI）->凑齐一桌子->打牌->结束
	 */
	ENTER,	//进入房间
	BEGIN ,	//开局
	WAITTING,	//等待玩家
	READY,		//凑齐一桌子
	PLAY,		//打牌
	END;		//结束
}
