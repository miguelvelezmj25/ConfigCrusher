cc = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/email/comparison/config_crusher_brute_force.csv")
fw = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/email/comparison/feature_wise_brute_force.csv")
pw = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/email/comparison/pair_wise_brute_force.csv")
fb = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/email/comparison/splat_brute_force.csv")

names(cc)

boxplot(list(cc=cc[cc$measured=="false",]$relative.error,
             fw=fw[fw$measured=="false",]$relative.error,
             pw=pw[pw$measured=="false",]$relative.error,
             fb=fb[fb$measured=="false",]$relative.error))

df = data.frame(error = c(cc[cc$measured=="false",]$relative.error,
                          fw[fw$measured=="false",]$relative.error,
                          pw[pw$measured=="false",]$relative.error,
                          fb[fb$measured=="false",]$relative.error),
                treat = c(rep("cc", length(cc[cc$measured=="false",]$relative.error)),
                          rep("fw", length(fw[fw$measured=="false",]$relative.error)),
                          rep("pw", length(pw[pw$measured=="false",]$relative.error)),
                          rep("fb", length(fb[fb$measured=="false",]$relative.error))))

View(df)

library(nparcomp)

b = nparcomp(error ~ treat, data=df, asy.method = "mult.t",
            type = "Tukey", alternative = "two.side", 
            plot.simci = TRUE, info = FALSE)

summary(b)

pdf('../../../../../../../../resources/evaluation/programs/java/email/plots/comparison.pdf',width=9,height=9)
par(cex = 1, cex.main = 1.5, cex.axis = 1.2, cex.sub = 1.2)
plot(b)
title(sub='Email',line = 3)
dev.off()
