package uicontrols.datepicker.entities;

public class CN extends Language {
    @Override
    public String[] monthTitles() {
        return new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    }

    @Override
    public String[] weekTitles(int type) {
        switch (type) {
            case 0:
            return new String[]{"日", "一", "二", "三", "四", "五", "六"};
            case 1:
                return new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            default:
                return new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        }
    }

    @Override
    public String ensureTitle() {
        return "确定";
    }
}
