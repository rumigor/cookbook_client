package ru.rumigor.cookbook.data.stomp.stomp.pathmatcher;


import ru.rumigor.cookbook.data.stomp.stomp.dto.StompHeader;
import ru.rumigor.cookbook.data.stomp.stomp.dto.StompMessage;

public class SimplePathMatcher implements PathMatcher {

    @Override
    public boolean matches(String path, StompMessage msg) {
        String dest = msg.findHeader(StompHeader.DESTINATION);
        if (dest == null) return false;
        else return path.equals(dest);
    }
}
