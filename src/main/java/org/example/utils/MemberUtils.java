package org.example.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class MemberUtils {
    public static boolean isAdmin(Member member) {
        return member != null && (member.hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR));
    }
}
