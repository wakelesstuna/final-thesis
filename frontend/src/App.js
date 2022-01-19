// React
import { Route, Routes, useLocation } from "react-router-dom";
// Recoil
import { useSetRecoilState } from "recoil";
import { atomUser } from "./atom/atomStates";
// Components
import NavBar from "./components/nav/NavBar";
// Styles

import { PageWrapper } from "./styles/layout";
// Pages
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import MessagePage from "./pages/MessagePage";
import ProfilePage from "./pages/ProfilePage";
import PostPage from "./pages/PostPage";
import DiscoverPage from "./pages/DiscoverPage";
import SignUpPage from "./pages/SignUpPage";
import AccountEditPage from "./pages/accounts/AccountEditPage";
import AccountPasswordChangePage from "./pages/accounts/AccountPasswordChangePage";
import AccountMangeAccessPage from "./pages/accounts/AccountMangeAccessPage";
import AccountEmailSettingsPage from "./pages/accounts/AccountEmailSettingsPage";
import AccountPushNotificationPage from "./pages/accounts/AccountPushNotificationPage";
import AccountContactHistoryPage from "./pages/accounts/AccountContactHistoryPage";
import AccountPrivacyPage from "./pages/accounts/AccountPrivacyPage";
import AccountLoginPage from "./pages/accounts/AccountLoginPage";
import AccountEmailSentPage from "./pages/accounts/AccountEmailSentPage";
import AccountHelpPage from "./pages/accounts/AccountHelpPage";

function App() {
  const setUser = useSetRecoilState(atomUser);
  const { pathname } = useLocation();

  window.onload = function () {
    const user = JSON.parse(localStorage.getItem("user"));
    setUser(user);
  };

  const checkPathsForNavbar = () => {
    return pathname === "/login" || pathname === "/sign-up";
  };

  return (
    <div>
      {checkPathsForNavbar() ? null : <NavBar />}
      <PageWrapper>
        <Routes>
          <Route path='/' element={<HomePage />} />
          <Route path='/login' element={<LoginPage />} />
          <Route path='/messages' element={<MessagePage />} />
          <Route
            path='/profile/:profileId/bookmarks'
            element={<ProfilePage />}
          />
          <Route path='/profile' element={<ProfilePage />} />
          <Route path='/profile/:profileId' element={<ProfilePage />} />
          <Route path='/post/:postId' element={<PostPage />} />
          <Route path='/accounts/edit/' element={<AccountEditPage />} />
          <Route
            path='/accounts/password/change'
            element={<AccountPasswordChangePage />}
          />
          <Route
            path='/accounts/mange_access'
            element={<AccountMangeAccessPage />}
          />
          <Route
            path='/accounts/emails/settings'
            element={<AccountEmailSettingsPage />}
          />
          <Route
            path='/accounts/push/web/settings'
            element={<AccountPushNotificationPage />}
          />
          <Route
            path='/accounts/contact_history'
            element={<AccountContactHistoryPage />}
          />
          <Route
            path='/accounts/privacy_and_security'
            element={<AccountPrivacyPage />}
          />
          <Route
            path='/accounts/session/login_activity'
            element={<AccountLoginPage />}
          />
          <Route
            path='/accounts/emails/emails_sent'
            element={<AccountEmailSentPage />}
          />
          <Route path='/accounts/settings/help' element={<AccountHelpPage />} />

          <Route path='/discover' element={<DiscoverPage />} />
          <Route path='/sign-up' element={<SignUpPage />} />
        </Routes>
      </PageWrapper>
    </div>
  );
}

export default App;
