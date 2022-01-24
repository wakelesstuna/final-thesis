// GraphQl
import { useQuery } from "@apollo/client";
import { getPosts_gql } from "../graphql/query";
// Styles
import { Page } from "../styles/layout";
// Components
import DiscoverImagesOne from "../components/discoverImages/DiscoverImagesOne";
import DiscoverImagesThree from "../components/discoverImages/DiscoverImagesThree";
import DiscoverImagesTwo from "../components/discoverImages/DiscoverImagesTwo";
import ErrorSwalMessage from "../components/util/ErrorSwalMessage";
import LoadingDots from "../components/util/LoadingDots";

const DiscoverPage = () => {
  const { data, error, loading, fetchMore } = useQuery(getPosts_gql, {
    variables: {
      first: 30,
      after: null,
    },
  });

  if (error) return <ErrorSwalMessage error={error} />;
  if (loading || !data) return <LoadingDots />;

  const arrOfPosts = data.paginationPosts.edges.map((edge) => edge.node);

  const arr = splitToArrayOfThree(arrOfPosts);
  let count = 0;
  return (
    <Page>
      {arr &&
        arr.map((imageList, i) => {
          if (count < 1) {
            count++;
            return <DiscoverImagesOne key={i * 2134} posts={imageList} />;
          } else if (count < 2) {
            count++;
            return <DiscoverImagesTwo key={i * 214334} posts={imageList} />;
          } else if (count < 3) {
            count++;
            return <DiscoverImagesTwo key={i * 2134} posts={imageList} />;
          } else if (count < 4) {
            count++;
            return <DiscoverImagesThree key={i * 2193} posts={imageList} />;
          } else {
            count = 0;
          }
          return null;
        })}
    </Page>
  );
};

const splitToArrayOfThree = (arr) => {
  let i = 0;
  let res = [];
  let tempArr = [];

  while (i < arr.length) {
    if (i !== 0 && i % 3 === 0) {
      res.push(tempArr);
      tempArr = [arr[i]];
    } else {
      tempArr.push(arr[i]);
    }
    i++;
  }
  res.push(tempArr);
  return res;
};

export default DiscoverPage;
