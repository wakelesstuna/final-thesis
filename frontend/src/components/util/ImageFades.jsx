// React
import { useEffect, useState } from "react";
// Styles
import styled from "styled-components";

const heroImage = [
  "https://www.instagram.com/static/images/homepage/screenshot1.jpg/d6bf0c928b5a.jpg",
  "https://www.instagram.com/static/images/homepage/screenshot4.jpg/842fe5699220.jpg",
  "https://www.instagram.com/static/images/homepage/screenshot3.jpg/f0c687aa6ec2.jpg",
  "https://www.instagram.com/static/images/homepage/screenshot5.jpg/0a2d3016f375.jpg",
  "https://www.instagram.com/static/images/homepage/screenshot2.jpg/6f03eb85463c.jpg",
];

const ImageFades = () => {
  const [activeIndex, setactiveIndex] = useState(0);

  useEffect(() => {
    const timeOut = setTimeout(() => {
      if (activeIndex === heroImage.length - 1) {
        setactiveIndex(0);
      } else {
        setactiveIndex(activeIndex + 1);
      }
    }, 3000);
    return () => clearTimeout(timeOut);
  }, [activeIndex]);

  const showImage = (number) => {
    return number === activeIndex ? 1 : 0;
  };

  return (
    <>
      <ImageFade
        src={heroImage[0]}
        alt={"imageTitle"}
        style={{ opacity: showImage(0) }}
      />
      <ImageFade
        src={heroImage[1]}
        alt={"imageTitle"}
        style={{ opacity: showImage(1) }}
      />
      <ImageFade
        src={heroImage[2]}
        alt={"imageTitle"}
        style={{ opacity: showImage(2) }}
      />
      <ImageFade
        src={heroImage[3]}
        alt={"imageTitle"}
        style={{ opacity: showImage(3) }}
      />
      <ImageFade
        src={heroImage[4]}
        alt={"imageTitle"}
        style={{ opacity: showImage(4) }}
      />
    </>
  );
};

export default ImageFades;

const ImageFade = styled.img`
  position: absolute;
  transition: opacity 1s;
`;
