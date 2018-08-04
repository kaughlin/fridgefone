package codepath.kaughlinpractice.fridgefone;

import java.util.HashSet;

public class Singleton {
    //static variable of single_instance of type Singleton
    private static Singleton sSingleInstance = null;

    //shared data variables
    private HashSet<String> mSelectedNamesSet;
    private HashSet<String> mAllItemNamesSet;
    private String mSelectedItemsString;
    private String mAllFridgeItemsString;
    private boolean mAllSelected;
    private boolean mSelectItemsBoolean;
    private boolean mNoneSelected;

    // private constructor restricted to class itself
    private Singleton(){
        mSelectedNamesSet = new HashSet<>();
        mAllItemNamesSet = new HashSet<>();
        mAllSelected = false;
        mSelectItemsBoolean = false;
        mNoneSelected = true;
    }

    // static method to create instance of singleton class
    public static Singleton getSingletonInstance(){
        //To ensure only one instance is created
        if(sSingleInstance == null){
            sSingleInstance = new Singleton();
        }
        return sSingleInstance;
    }


    //getters
    public HashSet<String> getmSelectedNamesSet() {
        return mSelectedNamesSet;
    }

    public HashSet<String> getmAllItemNamesSet() {
        return mAllItemNamesSet;
    }

    public String getmSelectedItemsString() { return mSelectedItemsString; }

    public boolean ismAllSelected() {
        return mAllSelected;
    }

    public boolean ismSelectItemsBoolean() {
        return mSelectItemsBoolean;
    }

    public boolean ismNoneSelected() {
        return mNoneSelected;
    }

    public String getmAllFridgeItemsString() {
        return mAllFridgeItemsString;
    }

    // setters
    public void setmAllSelected(boolean mAllSelected) {
        this.mAllSelected = mAllSelected;
    }

    public void setmSelectItemsBoolean(boolean mSelectItemsBoolean) {
        this.mSelectItemsBoolean = mSelectItemsBoolean;
    }

    public void setmSelectedItemsString(String mSelectedItemsString) {
        this.mSelectedItemsString = mSelectedItemsString;
    }

    public void setmNoneSelected(boolean mNoneSelected) {
        this.mNoneSelected = mNoneSelected;
    }

    public void setmAllFridgeItemsString(String mAllFridgeItemsString) {
        this.mAllFridgeItemsString = mAllFridgeItemsString;
    }
}
