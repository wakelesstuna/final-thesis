/**
 * This file holds the query functions for the graphql requests
 */

// GraphQL
import { gql } from "@apollo/client";

export const getUserById_gql = gql`
  query User($id: UUID) {
    user(id: $id) {
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

export const getPosts_gql = gql`
  query PaginationPosts($first: Int!, $after: String) {
    paginationPosts(first: $first, after: $after) {
      edges {
        cursor
        node {
          id
          imageUrl
          caption
          createdAt
          user {
            id
            username
            profilePic
          }
          totalComments
          comments {
            id
            comment
            user {
              username
            }
          }
          totalLikes
          userLikes {
            id
          }
        }
      }
      pageInfo {
        hasPreviousPage
        hasNextPage
        startCursor
        endCursor
      }
    }
  }
`;

export const getPostById_gql = gql`
  query Post($postId: UUID) {
    post(postId: $postId) {
      id
      imageUrl
      caption
      createdAt
      user {
        id
        username
        profilePic
      }
      totalComments
      comments {
        id
        comment
        user {
          username
        }
      }
      totalLikes
      userLikes {
        id
      }
    }
  }
`;

export const getCommentsOfPostById_gql = gql`
  query Post($postId: UUID) {
    post(postId: $postId) {
      comments {
        id
        comment
        user {
          username
        }
      }
    }
  }
`;

export const getPostsOfUser_gql = gql`
  query GetPostOfUser($id: UUID) {
    user(id: $id) {
      posts {
        id
        imageUrl
        totalLikes
        totalComments
      }
    }
  }
`;

export const getRandomUsers_gql = gql`
  query ($howMany: Int!, $username: String!) {
    fetchRandomUsers(howMany: $howMany, username: $username) {
      id
      username
      firstName
      lastName
      profilePic
      followers {
        id
        username
      }
    }
  }
`;

export const fetchBookmarksQuery_gql = gql`
  query User($id: UUID!) {
    user(id: $id) {
      id
      bookmarks {
        id
        imageUrl
        totalLikes
        totalComments
      }
    }
  }
`;

export const fetchUserFollowers_gql = gql`
  query Query($userId: UUID) {
    user(id: $userId) {
      followers {
        id
        username
        firstName
        lastName
        profilePic
      }
    }
  }
`;

export const fetchUserFollowing_gql = gql`
  query Query($userId: UUID) {
    user(id: $userId) {
      following {
        id
        username
        firstName
        lastName
        profilePic
      }
    }
  }
`;

export const isBookmarked_gql = gql`
  query ($bookmarkInput: BookmarkInput) {
    isBookmarked(bookmarkInput: $bookmarkInput)
  }
`;

export const isPostLiked_gql = gql`
  query ($likeInput: LikeInput) {
    isPostLiked(likeInput: $likeInput)
  }
`;

export const exitsByUsername_gql = gql`
  query ExitsByUsername($username: String) {
    exitsByUsername(username: $username)
  }
`;
