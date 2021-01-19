module havis.middleware.tdt {
    requires jackson.databind;
    requires java.logging;
    requires java.xml;

    requires transitive jaxb.api;

    exports havis.middleware.misc;
    exports havis.middleware.tdt;

}
