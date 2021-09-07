package com.example.renyanyu;




public class GlobalParms {
    public static BlankFragment1 f1; //主页fragemnt
    public static BlankFragment2 f2; //走势图fragemnt
    public static BlankFragment3 f3; //资讯fragemnt
    public static ChangeFragment sChangeFragment;  //改变选中Frangment的接口

    /**
     * 获取主页Fragment
     *
     * @return
     */
    public static BlankFragment1 getHomeFragment() {
        if (f1 == null) {
            f1 = new BlankFragment1();
        }
        return f1;
    }

    /**
     * 走势图fragemnt
     *
     * @return
     */
    public static BlankFragment2 getChartsFragment() {
        if (f2 == null) {
            f2 = new BlankFragment2();
        }
        return f2;
    }

    /**
     * 资讯fragemnt
     *
     * @return
     */
    public static BlankFragment3 getZiXunFragment() {
        if (f3 == null) {
            f3 = new BlankFragment3();
        }
        return f3;
    }

    /**
     * //其它fragemnt
     *
     * @return
     */


    /**
     * 设置被选中的Fragment
     * @param changeFragment
     */
    public static void setFragmentSelected(ChangeFragment changeFragment) {
        sChangeFragment = changeFragment;

    }
}
