package Game;

public enum ErrorType
{
	loadFile_NullPtrEx, loadFile_null, readAndCloseFile_IOEx, closeFile_IOEx, findLayoutLine_noLine,
	assignParameter_uneqVal, assignParameter_unknVal, assignParameter_appearMulti, assignParameter_noValForList,
	assignParameter_negVal, assignParameter_unknParamName, loadParameter_unsetParam,
	loadResources_missingFile, loadResources_otherEx,
	ImageSet_nullStr, MusicPlayer_load_JLayerEx, MusicPlayer_play_JLayerEx,
	SFXPlayer_nullStr,
	Level_loadLayout_uneqRowNum, Level_loadLayout_uneqEleNum, Level_loadLayout_twoStartEnd, Level_loadLayout_unknChar,
	Level_closeMusic_failThreadJoin,
	StartMenu_loadResources_NullPtrEx, StartMenu_loadResources_IOEx
}
