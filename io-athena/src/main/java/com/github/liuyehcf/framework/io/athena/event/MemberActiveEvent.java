package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class MemberActiveEvent extends AbstractMemberEvent {

    public MemberActiveEvent(Member member) {
        super(member);
    }
}
