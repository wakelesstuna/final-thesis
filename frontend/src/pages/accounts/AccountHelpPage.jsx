// Components
import AccountNavMenu from "../../components/accounts/AccountNavMenu";
// Styles
import {
  AccountPageWrapper,
  Center,
  InformationSection,
} from "../../styles/layout";

const AccountHelpPage = () => {
  return (
    <AccountPageWrapper>
      <AccountNavMenu />
      <InformationSection>
        <Center>
          <p>
            Due to this is not the real instagram and <br />
            just a clone for a Thesis there is no help page
          </p>
        </Center>
      </InformationSection>
    </AccountPageWrapper>
  );
};

export default AccountHelpPage;
