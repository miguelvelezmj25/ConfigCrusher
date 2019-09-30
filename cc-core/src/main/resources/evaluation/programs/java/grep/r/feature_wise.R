feature_wise <- read.csv("src/main/resources/evaluation/programs/java/grep/r/feature_wise.csv")
model <- lm(time~FIXEDSTRINGS+INVERTMATCH+WHOLELINE+COUNT+LINENUMBER+MATCHINGFILES+IGNORECASE, data = feature_wise)
coef(model)
