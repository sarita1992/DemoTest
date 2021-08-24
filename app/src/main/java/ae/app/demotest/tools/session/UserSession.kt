package ae.app.demotest.tools.session

object UserSession {
    var token: String? = null
    var refreshToken: String? = null

    fun newSession(token: String, refreshToken: String) {
        UserSession.token = token
        UserSession.refreshToken = refreshToken
    }

    fun clear() {
        token = null
        refreshToken = null
    }
}