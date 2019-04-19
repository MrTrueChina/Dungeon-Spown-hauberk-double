# 基于 hauberk 的支持双数宽高和房间位置的地牢生成算法

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

# 简介：
这是一个基于 Bob Nystrom 的 hauberk 的地牢生成算法写出的算法，原版算法思路十分精妙，其 房间-迷宫-通道 的设计令我大开眼界，简直就是打开了新世界的大门。但原版算法有一个很小但让强迫症患者们不太舒服的缺点：只能生成宽高都为单数、房间宽高坐标也都为单数的地图。我用Java把这个算法模仿了一下，在细节上做一些调整，让其可以兼容双数。<br/>
在看代码之前强力推荐去看原版的详解：<br/>
<br/>
<b>原版：<a href = "http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/">Rooms and Mazes: A Procedural Dungeon Generator</a></b><br/>
<b>中文版：<a href="https://indienova.com/indie-game-development/rooms-and-mazes-a-procedural-dungeon-generator/">房间和迷宫：一个地牢生成算法</a></b><br/>
