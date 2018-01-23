feature_wise <- read.csv("src/main/resources/evaluation/programs/java/kanzi/r/feature_wise.csv")
model <- lm(time~CHECKSUM+FORCE+BLOCKSIZE+TRANSFORM+VERBOSE+LEVEL+ENTROPY, data = feature_wise)
coef(model)
