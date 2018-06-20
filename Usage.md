# How to use?
1. Include AREditor into your project. For now there is no gradle dependency support to do this step automatically, so you have to:
	- Get: Clone or download from [Github ARE](https://github.com/chinalwb/Android-Rich-text-Editor)，then unzip
	- Import: Open Android Studio, click File > New > Import Module..., choose the 'are' folder which you get in the above step, then click 'Finish'
	- Denpendency: In Android Studio, right click on the Project or module you want to use ARE, in the context menu click 'Open Module Settings', in the last tab 'Denpendencies', click the '+' icon at the left bottom, choose '3. Module Dependency', in the popup window, choose 'are'.
	- Use it: In the activity which you want to render the ARE, open its layout xml, add AREditor into it, for example:
	
	```
	<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <YOUR_LAYOUT_ABOVE_EDITOR />

    <com.chinalwb.are.AREditor
        android:id="@+id/areditor"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/xx"
        android:background="@android:color/white" />
</RelativeLayout>
    ```
  
2. Run your project and start your editing. If you want to export the editing to HTML, see `getHtml` in '3. API' below
3. API
	- *fromHtml* com.chinalwb.are.AREditor#fromHtml(String html) -- load the existing html content into editor 
	- *getHtml* com.chinalwb.are.AREditor#getHtml() -- return the editing text in HTML format
4. Customization
	- *@*: Currently, there is a built-in @ activity to show the @ people list, if you want to customize you need to create your own `AtStrategy`, and implement two methods:

	```
	public interface AtStrategy {
    /**
     * In this method, you want to start an activity to show a list for user to pick up.
     */
    void openAtPage();

    /**
     * ARE has a default behaviour for handling the event when user selects an {@AtItem}.
     *
     * If you want to customize, you can do it in this method and return true;
     * or else if you want to use the default implementation, just return false, then ARE
     * will take care the event.
     *
     * @param item
     * @return
     */
    boolean onItemSelected(AtItem item);
	}
```
	A sample:

	```
    private AtStrategy mAtStrategy = new AtStrategy() {
        @Override
        public void openAtPage() {
            Intent intent = new Intent(MainActivity.this, AtActivity.class);
            startActivityForResult(intent, ARE_Toolbar.REQ_AT);
        }

        @Override
        public boolean onItemSelected(AtItem item) {
            return false;
        }
    };
    
    <YOUR_AREditor>.setAtStrategy(mAtStrategy);
	```

- *Video*: You can insert Video into ARE, if you want upload the video from local to server-side, similarly to *@*, you can do the video uploading by setting the `VideoStrategy`. A sample:
	
```
	private VideoStrategy mVideoStrategy = new VideoStrategy() {
        @Override
        public String uploadVideo(Uri uri) {
            try {
                Thread.sleep(3000); // Do upload here
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "http://www.xx.com/x.mp4";
        }
    };
    
    <YOUR_AREditor>.setVideoStrategy(mVideoStrategy);
```

- Don't need to show all styles? Want to change the color of toolbar? Want to change the order of the buttons in toolbar? Want to change the default(ugly) toolbar buttons? -- Sorry no API supported for these features for now, you need to open `are_toolbar.xml` in `are/res/layout`, then do the changes you need, just attention that you can move the buttons `<ImageView />` up and down, you can hide it by setting `android:visibility="gone"`, but don't change the `id`, if you do want to do that you need to know there are other places to change accordingly too.