/**
 * This file holds the mutation functions for the graphql requests
 */

// GraphQL
import gql from "graphql-tag";

export const createUserMutation_gql = gql`
  mutation CreateUser($createUserInput: CreateUserInput, $input: Upload) {
    createUser(createUserInput: $createUserInput, input: $input) {
      id
      username
      firstName
      lastName
      email
      profilePic
    }
  }
`;

export const updateUser_gql = gql`
  mutation UpdateUser($updateUserInput: UpdateUserInput, $input: Upload) {
    updateUser(updateUserInput: $updateUserInput, input: $input) {
      id
      username
      firstName
      lastName
      profilePic
      email
      phone
      description
      totalFollowing
      totalFollowers
      totalPosts
    }
  }
`;

export const authUser_gql = gql`
  mutation AuthUser($authUserInput: AuthUserInput) {
    authUser(authUserInput: $authUserInput) {
      id
      username
      firstName
      lastName
      profilePic
      email
      phone
      description
      totalFollowing
      totalFollowers
      totalPosts
    }
  }
`;

export const updatePassword_gql = gql`
  mutation UpdatePassword($updatePasswordInput: UpdatePasswordInput) {
    updatePassword(updatePasswordInput: $updatePasswordInput)
  }
`;

export const createPostMutation_gql = gql`
  mutation CreatePost($createPostInput: CreatePostInput, $input: Upload) {
    createPost(createPostInput: $createPostInput, input: $input) {
      id
    }
  }
`;

export const deletePostMutation_gql = gql`
  mutation Mutation($postInput: PostInput!) {
    deletePost(postInput: $postInput)
  }
`;

export const createComment_gql = gql`
  mutation CreateComment($createCommentInput: CreateCommentInput) {
    createComment(createCommentInput: $createCommentInput) {
      id
    }
  }
`;

export const deleteComment_gql = gql`
  mutation DeleteComment($commentInput: CommentInput) {
    deleteComment(commentInput: $commentInput)
  }
`;

export const followUser_gql = gql`
  mutation ($followInput: FollowInput) {
    followUser(followInput: $followInput)
  }
`;

export const unFollowUser_gql = gql`
  mutation ($followInput: FollowInput) {
    unFollowUser(followInput: $followInput)
  }
`;

export const likePost_gql = gql`
  mutation ($likeInput: LikeInput) {
    likePost(likeInput: $likeInput)
  }
`;

export const unLikePost_gql = gql`
  mutation ($likeInput: LikeInput) {
    unLikePost(likeInput: $likeInput)
  }
`;

export const bookmarkPost_gql = gql`
  mutation ($bookmarkInput: BookmarkInput) {
    bookmarkPost(bookmarkInput: $bookmarkInput)
  }
`;

export const unbookmarkPost_gql = gql`
  mutation ($bookmarkInput: BookmarkInput) {
    unBookmarkPost(bookmarkInput: $bookmarkInput)
  }
`;
