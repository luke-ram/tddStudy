package chap02self;

import chap02.PasswordStrength;

public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        int metCounts =0;
        if (s.length() >= 8) {
            metCounts++;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);
        if (containsNum){
            metCounts++;
        }

        boolean containsUpperCase = isContainsUpperCase(s);
        if(containsUpperCase){
            metCounts ++;
        }

        if (metCounts <= 1) return PasswordStrength.WEAK;
        if (metCounts == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean isContainsUpperCase(String s) {
        boolean containsUpperCase = false;
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpperCase =  true;
            }
        }
        return containsUpperCase;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
