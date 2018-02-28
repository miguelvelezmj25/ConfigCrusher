cc = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/density/comparison/config_crusher_brute_force.csv")
fw = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/density/comparison/feature_wise_brute_force.csv")
pw = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/density/comparison/pair_wise_brute_force.csv")
sp = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/density/comparison/splat_brute_force.csv")
fb = read.csv("~/Documents/Programming/Java/Projects/ConfigCrusher/src/main/resources/evaluation/programs/java/grep/comparison/family_brute_force.csv")

names(cc)

boxplot(list(cc=cc[cc$measured=="false",]$relative.error,
             fw=fw[fw$measured=="false",]$relative.error,
             pw=pw[pw$measured=="false",]$relative.error,
             sp=sp[sp$measured=="false",]$relative.error))

df = data.frame(error = c(cc[cc$measured=="false",]$relative.error,
                          fw[fw$measured=="false",]$relative.error,
                          pw[pw$measured=="false",]$relative.error), 
                treat = c(rep("cc", length(cc[cc$measured=="false",]$relative.error)),
                          rep("fw", length(fw[fw$measured=="false",]$relative.error)),
                          rep("pw", length(pw[pw$measured=="false",]$relative.error))))

#df = data.frame(error = c(cc[cc$measured=="false",]$relative.error,
#                          fw[fw$measured=="false",]$relative.error,
#                          pw[pw$measured=="false",]$relative.error,
#                          sp[sp$measured=="false",]$relative.error), 
#                treat = c(rep("cc", length(cc[cc$measured=="false",]$relative.error)),
#                          rep("fw", length(fw[fw$measured=="false",]$relative.error)),
#                          rep("pw", length(pw[pw$measured=="false",]$relative.error)),
#                          rep("sp", length(sp[sp$measured=="false",]$relative.error))))

View(df)

library(nparcomp)

b = nparcomp(error ~ treat, data=df, asy.method = "mult.t",
            type = "Tukey", alternative = "two.side", 
            plot.simci = TRUE, info = FALSE)
summary(b)





