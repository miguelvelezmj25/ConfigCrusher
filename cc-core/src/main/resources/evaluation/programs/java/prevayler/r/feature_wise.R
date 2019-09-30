feature_wise <- read.csv("src/main/resources/evaluation/programs/java/prevayler/r/feature_wise.csv")
model <- lm(time~FILEAGETHRESHOLD+DEEPCOPY+TRANSIENTMODE+MONITOR+DISKSYNC+SNAPSHOTSERIALIZER+JOURNALSERIALIZER+FILESIZETHRESHOLD+CLOCK, data = feature_wise)
coef(model)
