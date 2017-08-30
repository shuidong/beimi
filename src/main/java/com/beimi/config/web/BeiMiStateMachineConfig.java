package com.beimi.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.beimi.core.engine.game.BeiMiGameEnum;
import com.beimi.core.engine.game.BeiMiGameEvent;
import com.beimi.core.engine.game.action.EnterAction;
import com.beimi.core.engine.game.action.EventAction;
import com.beimi.core.statemachine.BeiMiStateMachine;
import com.beimi.core.statemachine.config.StateConfigurer;
import com.beimi.core.statemachine.config.StateMachineTransitionConfigurer;

@Configuration
public class BeiMiStateMachineConfig<T, S>  {
	
	@Bean
	public BeiMiStateMachine<String,String> create() throws Exception{
		BeiMiStateMachine<String,String> beiMiStateMachine = new BeiMiStateMachine<String,String>();
		this.configure(beiMiStateMachine.getConfig());
		this.configure(beiMiStateMachine.getTransitions());
		return beiMiStateMachine;
	}
	
    public void configure(StateConfigurer<String,String> states)
            throws Exception {
        states
            .withStates()
                .initial(BeiMiGameEnum.NONE.toString())
                    .state(BeiMiGameEnum.CRERATED.toString())
                    .state(BeiMiGameEnum.WAITTING.toString())
                    .state(BeiMiGameEnum.READY.toString())
                    .state(BeiMiGameEnum.BEGIN.toString())
                    .state(BeiMiGameEnum.PLAY.toString())
                    .state(BeiMiGameEnum.END.toString());
	}

    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
		EventAction action = new EventAction();
		/**
		 * 状态切换：BEGIN->WAITTING->READY->PLAY->END
		 */
        transitions
	        .withExternal()	
		    	.source(BeiMiGameEnum.NONE.toString()).target(BeiMiGameEnum.CRERATED.toString())
		    	.event(BeiMiGameEvent.ENTER.toString()).action(new EnterAction<String,String>())
		    	.and()
		    .withExternal()	
	        	.source(BeiMiGameEnum.CRERATED.toString()).target(BeiMiGameEnum.WAITTING.toString())
	        	.event(BeiMiGameEvent.JOIN.toString()).action(action)
	        	.and()
            .withExternal()	
                .source(BeiMiGameEnum.WAITTING.toString()).target(BeiMiGameEnum.READY.toString())
                .event(BeiMiGameEvent.ENOUGH.toString()).action(action)
                .and()
            .withExternal()
                .source(BeiMiGameEnum.READY.toString()).target(BeiMiGameEnum.BEGIN.toString())
                .event(BeiMiGameEvent.AUTO.toString()).action(action)
                .and()
            .withExternal()
                .source(BeiMiGameEnum.BEGIN.toString()).target(BeiMiGameEnum.PLAY.toString())
                .event(BeiMiGameEvent.RAISEHANDS.toString()).action(action)
                .and()
            .withExternal()
                .source(BeiMiGameEnum.PLAY.toString()).target(BeiMiGameEnum.END.toString())
                .event(BeiMiGameEvent.ALLCARDS.toString()).action(action)
            ;
    }
}
