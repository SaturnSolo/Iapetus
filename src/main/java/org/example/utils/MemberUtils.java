package org.example.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class MemberUtils {
    public static boolean hasAdminPermission(Member member) {
        return member != null && member.hasPermission(Permission.MANAGE_CHANNEL, Permission.ADMINISTRATOR, Permission.MANAGE_SERVER);
    }
}
