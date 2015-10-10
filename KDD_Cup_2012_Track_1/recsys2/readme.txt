https://www.kddcup2012.org/c/kddcup2012-track1

一、Recommender Systems部分

1、GenTrainAndTest.java是生成训练集和测试集（按7:3的比例）。输入文件是rec_log_train.txt（73209277行），输出文件是train（51246705行）和test（21962572行）。

2、GenTrainChange.java是将用户对项目的反馈都写到一行，即key是用户，value是该用户所有进行过反馈的项目（包含是正反馈还是负反馈的信息）。输入文件是train_sort（对train执行sort后的文件，51246705行），输出文件是train_change（1386745行）。

3、ExtractUserTagsForLDA.java是提取所有用户的tag信息并整理成LDA输入文件的格式。该类输入文件是user_profile.txt（2320895行），输出文件是user_tags（771651行）和user_tags_LDAFormat（771652行）。

4、使用JGibbLDA-v.1.0对user_tags_LDAFormat进行训练，训练参数为30类，每类50个词，迭代50次。输出文件为model-final.theta（771651行）。LDA程序参数如下：
-est -alpha 0.5 -beta 0.1 -ntopics 30 -niters 50 -savestep 100 -twords 50 -dir D:\JAVA\Workspaces\JGibbLDA-v.1.0\models\track1 -dfile user_tags_LDAFormat

5、GetUserTagsGroupFromLDAResult.java是从LDA的训练结果中获取每个用户的tag类别。输入文件时user_tags和model-final.theta，输出文件是user_tags_group（771651行）。

6、GenUserFeatures.java是生成所有用户的特征，这些特征依次是年龄、性别、微博数量、tag类别。输入文件是user_tags_group和user_profile.txt，输出文件是user_features（2320895行）。

7、生成“项目-特征”的矩阵，每一行的key是项目，value是特征向量，向量的每个值包含的信息为该特征正反馈的用户数量和负反馈的用户数量。输入文件是user_features和train_change，输出文件是matrix（4697行）。

8、生成“项目-特征”的概率矩阵，同时按0.7的概率生成一个训练概率矩阵。输入文件是matrix，输出文件是matrix_iid（4697行）、matrix_probability（4697行）、matrix_probability_train（4697行）。


二、Social Network Analysis部分

1、GenUserSnsChange.java是生成一个用户对应的所有的sns联系人。输入文件为 user_sns.txt（50655143行），输出文件为user_sns_change（1892058行）。

2、GenUserSnsFeatures.java是sample链接关系及其对应的特征。