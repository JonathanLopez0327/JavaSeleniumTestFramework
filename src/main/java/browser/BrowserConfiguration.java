package browser;

public enum BrowserConfiguration {
    FIREFOX,
    CHROME,
    EDGE,
    OTHER_CHROME_VERSION,
    DOCKER_CHROME,
    DOCKER_FIREFOX,
    DOCKER_EDGE,
    BROWSERSTACK_CHROME,
    NONE;
    private BrowserConfiguration() {}
}
