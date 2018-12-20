package im.kai.server.service.message.validator;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.resolver.*;

public class ValidatorChainFactory {
    public static AbstractValidator getValidatorChainObj(WebSocketProtos.WebSocketMessage.Type type){
        AbstractValidator validatorChain = null;
        if(type.equals(WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE)){
            validatorChain = new FriendRelationshipValidator();
            AbstractValidator validatorCond2 = new UserSettingValidator();
            validatorChain.setNextAbstract(validatorCond2);
        }else if (type.equals(WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE)){

        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.MESSAGE_NOTICE)){

        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.USER_NOTICE)){

        }
        return validatorChain;
    }
}
