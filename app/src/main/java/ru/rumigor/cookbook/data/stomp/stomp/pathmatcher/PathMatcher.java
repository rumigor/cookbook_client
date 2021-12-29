package ru.rumigor.cookbook.data.stomp.stomp.pathmatcher;


import ru.rumigor.cookbook.data.stomp.stomp.dto.StompMessage;

public interface PathMatcher {

    boolean matches(String path, StompMessage msg);
}
