// Components
import AccountNavMenu from "../../components/accounts/AccountNavMenu";
// Styles
import {
  AccountPageWrapper,
  Center,
  InformationSection,
} from "../../styles/layout";

const AccountPrivacyPage = () => {
  return (
    <AccountPageWrapper>
      <AccountNavMenu />
      <InformationSection>
        <Center>
          <p>
            Instagram Stuff, <br /> Not implemented yet!
          </p>
        </Center>
      </InformationSection>
    </AccountPageWrapper>
  );
};

export default AccountPrivacyPage;
