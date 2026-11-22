package fr.jeudeloie.tv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Affiche le jeu (assets/www/index.html) en plein écran dans une WebView.
 * La télécommande envoie les flèches et OK à la page ; la touche Retour ouvre le menu du jeu,
 * et quitte l'application quand on appuie dessus depuis le menu.
 */
public class MainActivity extends Activity {
    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        preferHighestResolution();

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#0d1628"));
        web.setFocusable(true);
        web.setFocusableInTouchMode(true);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            s.setAllowFileAccessFromFileURLs(true);
        }

        web.addJavascriptInterface(new TvBridge(), "AndroidTV");
        setContentView(web);
        hideSystemUi();
        web.loadUrl("file:///android_asset/www/index.html?tv=1");
        web.requestFocus();
    }

    /**
     * Demande le mode d'affichage de plus haute définition (4K si le téléviseur le propose)
     * avec la même fréquence de rafraîchissement que le mode actuel.
     */
    private void preferHighestResolution() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        Display display = getWindowManager().getDefaultDisplay();
        Display.Mode current = display.getMode();
        Display.Mode best = current;
        for (Display.Mode m : display.getSupportedModes()) {
            boolean sameRate = Math.abs(m.getRefreshRate() - current.getRefreshRate()) < 1f;
            long pixels = (long) m.getPhysicalWidth() * m.getPhysicalHeight();
            long bestPixels = (long) best.getPhysicalWidth() * best.getPhysicalHeight();
            if (sameRate && pixels > bestPixels && m.getPhysicalHeight() <= 2160) best = m;
        }
        if (best.getModeId() != current.getModeId()) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.preferredDisplayModeId = best.getModeId();
            getWindow().setAttributes(lp);
        }
    }

    /** Taille physique de l'écran, lue par le jeu pour régler sa résolution de rendu. */
    private Point physicalSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Display.Mode mode = display.getMode();
            p.set(mode.getPhysicalWidth(), mode.getPhysicalHeight());
        } else {
            display.getRealSize(p);
        }
        return p;
    }

    private class TvBridge {
        @JavascriptInterface
        public int displayWidth() { return physicalSize().x; }

        @JavascriptInterface
        public int displayHeight() { return physicalSize().y; }
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUi();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                askPageForBack();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void askPageForBack() {
        if (web == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            finish();
            return;
        }
        web.evaluateJavascript("(window.tvBack ? window.tvBack() : false)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (!"true".equals(value)) finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (web != null) web.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (web != null) web.onResume();
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}
