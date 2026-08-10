package nl.kmartin.knitster.data.model

data class AppSettings(
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
    val appLocales: LanguageMode = LanguageMode.DEFAULT
)