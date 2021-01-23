package chap02;

public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean lengthEnough = s.length() >= 8;
        boolean containsNum = isContainsNum(s);
        boolean containsUpp = meetsContainingUppercaseCriteria(s);

        int metCounts = getMetCounts(lengthEnough, containsNum, containsUpp);

        if (metCounts <= 1) return PasswordStrength.WEAK;
        if (metCounts == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;

    }

    private int getMetCounts(boolean lengthEnough, boolean containsNum, boolean containsUpp) {
        int metCounts = 0;

        if (lengthEnough) {
            metCounts++;
        }

        if (containsNum) {
            metCounts++;
        }

        if (containsUpp) {
            metCounts++;
        }
        return metCounts;
    }

    private boolean meetsContainingUppercaseCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }


    private boolean isContainsNum(String s) {
        boolean containsNum = false;

        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                containsNum = true;
                break;
            }
        }
        return containsNum;
    }
}
