https://www.kddcup2012.org/c/kddcup2012-track1

cn.edu.jlu.zhangc10.recsys.preprocess1包：

1、GenTrainAndTest.java是生成训练集和测试集（按8:2的比例）。输入文件是rec_log_train.txt（73209277行），输出文件是train（58566417行）和test（14642860行）。

2、对train中执行sort命令按用户排序，输出文件是train_sort（58566417行）,执行的sort命令如下：sort -t $'\t' -k1 -n train > train_sort

3、GenTrainChange.java是将用户对项目的反馈都写到一行，即key是用户，value是该用户所有进行过反馈的项目（包含是正反馈还是负反馈的信息）。输入文件是train_sort，输出文件是train_change（1391099行）。

4、ExtractUserTagsForLDA.java是提取所有用户的tag信息并整理成LDA输入文件的格式。该类输入文件是user_profile.txt（2320895行），输出文件是user_tags（771651行）和user_tags_LDAFormat（771652行）。

5、使用JGibbLDA-v.1.0对user_tags_LDAFormat进行训练，训练参数为30类，每类100个词，迭代500次。输出文件为model-final.theta（771651行）。LDA程序参数如下：
-est -alpha 0.5 -beta 0.1 -ntopics 30 -niters 500 -savestep 100 -twords 100 -dir models/track1 -dfile user_tags_LDAFormat

6、GetUserTagsGroupFromLDA.java是从LDA的训练结果中获取每个用户的tag类别。输入文件时user_tags和model-final.theta，输出文件是user_tags_group（771651行）。

7、GenUserFeatures.java是生成所有用户的特征，这些特征依次是年龄、性别、微博数量、tag类别。输入文件是user_tags_group和user_profile.txt，输出文件是user_features（2320895行）。

8、GenFeaturesTimesList.java是统计每个用户特征出现的次数。输入文件是user_features，输出文件是features_times（1行）。

9、GenMatrix.java生成“项目-特征”的矩阵，每一行的key是项目，value是特征向量，向量的每个值包含的信息为该特征正反馈的用户数量和负反馈的用户数量。输入文件是user_features和train_change，输出文件是matrix（4702行）。

10、GenMatrixProbability.java生成“项目-特征”的概率矩阵，同时按0.8的概率生成一个训练概率矩阵。输入文件是matrix，输出文件是matrix_iid（4702行）、matrix_probability（4702行）、matrix_probability_train（4702行）。

-----

cn.edu.jlu.zhangc10.recsys.model包：

MuMethod.java : 用全局均值Mu来填充矩阵

MuBiBfMethod.java : 用全局均值Mu+项目偏置Bi+特征偏置Bf来填充矩阵

SVDMethod.java : 用奇异值分解SVD来填充矩阵

LFMMethod.java : 用潜语义模型来填充矩阵

CompareAllMethod.java : 比较上述四种模型的效果

-----

cn.edu.jlu.zhangc10.recsys.classify包：

1、LFM.java ： 用潜语义模型来填充矩阵

2、CalRatings.java ：计算用户对项目的偏好评分 

3、Classify.java ：进行分类

4、StatisticsClassification.java ：统计指标


-----


cn.edu.jlu.zhangc10.recsys.submission包：

1、sort -t $'\t' -k1 -n rec_log_train.txt > submission_train_sort

2、GenTrainChange.java

3、GenMatrix.java

4、GenMatrixProbability.java

5、LFM.java

6、CalRatings.java

7、sort -t $'\t' -k1 -n submission_ratings > submission_ratings_sort

8、GenSubmissionPart1.java

9、GenSubmissionPart2.java

10、cat submission_part_1 submission_part_2 > submission_final.csv

-----

cn.edu.jlu.zhangc10.recsys.preprocess2包：

GenTrainAndTest.java

sort -t $'\t' -k1 -n new_train > new_train_sort

sort -t $'\t' -k1 -n new_test > new_test_sort

GenTrainChange.java

GenMatrix.java

GenMatrixProbability.java

LFM.java

CalRatings.java

StaUserPredictClickItems.java

StaUserActualClickItems.java
