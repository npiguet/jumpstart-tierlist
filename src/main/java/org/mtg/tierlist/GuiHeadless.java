package org.mtg.tierlist;

import forge.gamemodes.match.HostedMatch;
import forge.gui.download.GuiDownloadService;
import forge.gui.interfaces.IGuiBase;
import forge.gui.interfaces.IGuiGame;
import forge.item.PaperCard;
import forge.localinstance.skin.FSkinProp;
import forge.localinstance.skin.ISkinImage;
import forge.sound.IAudioClip;
import forge.sound.IAudioMusic;
import forge.util.FSerializableFunction;
import forge.util.ImageFetcher;
import org.jupnp.UpnpServiceConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class GuiHeadless implements IGuiBase {

    private final String assetsDir;

    public GuiHeadless(String assetsDir) {
        this.assetsDir = assetsDir;
    }

    @Override
    public boolean isRunningOnDesktop() {
        return false;
    }

    @Override
    public boolean isLibgdxPort() {
        return false;
    }

    @Override
    public String getCurrentVersion() {
        return "";
    }

    @Override
    public String getAssetsDir() {
        return assetsDir;
    }

    @Override
    public ImageFetcher getImageFetcher() {
        return null;
    }

    @Override
    public void invokeInEdtNow(Runnable runnable) {

    }

    @Override
    public void invokeInEdtLater(Runnable runnable) {

    }

    @Override
    public void invokeInEdtAndWait(Runnable proc) {

    }

    @Override
    public boolean isGuiThread() {
        return false;
    }

    @Override
    public ISkinImage getSkinIcon(FSkinProp skinProp) {
        return null;
    }

    @Override
    public ISkinImage getUnskinnedIcon(String path) {
        return null;
    }

    @Override
    public ISkinImage getCardArt(PaperCard card) {
        return null;
    }

    @Override
    public ISkinImage getCardArt(PaperCard card, boolean backFace) {
        return null;
    }

    @Override
    public ISkinImage createLayeredImage(PaperCard card, FSkinProp background, String overlayFilename, float opacity) {
        return null;
    }

    @Override
    public void showBugReportDialog(String title, String text, boolean showExitAppBtn) {

    }

    @Override
    public void showImageDialog(ISkinImage image, String message, String title) {

    }

    @Override
    public int showOptionDialog(String message, String title, FSkinProp icon, List<String> options, int defaultOption) {
        return 0;
    }

    @Override
    public String showInputDialog(String message, String title, FSkinProp icon, String initialInput, List<String> inputOptions, boolean isNumeric) {
        return "";
    }

    @Override
    public <T> List<T> getChoices(String message, int min, int max, Collection<T> choices, Collection<T> selected, FSerializableFunction<T, String> display) {
        return List.of();
    }

    @Override
    public <T> List<T> order(String title, String top, int remainingObjectsMin, int remainingObjectsMax, List<T> sourceChoices, List<T> destChoices) {
        return List.of();
    }

    @Override
    public String showFileDialog(String title, String defaultDir) {
        return "";
    }

    @Override
    public File getSaveFile(File defaultFile) {
        return null;
    }

    @Override
    public void download(GuiDownloadService service, Consumer<Boolean> callback) {

    }

    @Override
    public void refreshSkin() {

    }

    @Override
    public void showCardList(String title, String message, List<PaperCard> list) {

    }

    @Override
    public boolean showBoxedProduct(String title, String message, List<PaperCard> list) {
        return false;
    }

    @Override
    public PaperCard chooseCard(String title, String message, List<PaperCard> list) {
        return null;
    }

    @Override
    public int getAvatarCount() {
        return 0;
    }

    @Override
    public int getSleevesCount() {
        return 0;
    }

    @Override
    public void copyToClipboard(String text) {

    }

    @Override
    public void browseToUrl(String url) throws IOException, URISyntaxException {

    }

    @Override
    public boolean isSupportedAudioFormat(File file) {
        return false;
    }

    @Override
    public IAudioClip createAudioClip(String filename) {
        return null;
    }

    @Override
    public IAudioMusic createAudioMusic(String filename) {
        return null;
    }

    @Override
    public void startAltSoundSystem(String filename, boolean isSynchronized) {

    }

    @Override
    public void clearImageCache() {

    }

    @Override
    public void showSpellShop() {

    }

    @Override
    public void showBazaar() {

    }

    @Override
    public IGuiGame getNewGuiGame() {
        return null;
    }

    @Override
    public HostedMatch hostMatch() {
        return null;
    }

    @Override
    public void runBackgroundTask(String message, Runnable task) {

    }

    @Override
    public String encodeSymbols(String str, boolean formatReminderText) {
        return "";
    }

    @Override
    public void preventSystemSleep(boolean preventSleep) {

    }

    @Override
    public float getScreenScale() {
        return 0;
    }

    @Override
    public UpnpServiceConfiguration getUpnpPlatformService() {
        return null;
    }
}
