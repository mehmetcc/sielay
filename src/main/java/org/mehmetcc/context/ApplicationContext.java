package org.mehmetcc.context;

public record ApplicationContext(Boolean didRunFillDatabaseBefore,
                                 String lastFillDatabaseContent,
                                 String seperator) {

}
