feature_wise <- read.csv("src/main/resources/evaluation/programs/java/pngtasticColorCounter/r/feature_wise.csv")
model <- lm(time~FREQTHRESHOLD+DISTTHRESHOLD+TIMEOUT+LOGLEVEL+MINALPHA, data = feature_wise)
coef(model)
