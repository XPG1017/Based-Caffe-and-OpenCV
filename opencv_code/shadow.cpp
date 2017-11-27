#include "cv.h"
#include "cxcore.h"
#include "highgui.h"
#include "math.h"
#include<opencv2/opencv.hpp>                   
#include "highgui.h"
#include "cvaux.h"
#include "cxcore.h"

using namespace cv;
using namespace std;

Mat sobel(Mat gray);
Mat canny(Mat src);

IplImage * get_grey( const char* src)  
{  
  
    const char* imagename = src;   
  
    IplImage * img = cvLoadImage(imagename);  
    if(!img)  
    {  
           fprintf(stderr, "Can not load image %s\n", imagename);  
           exit(-1);
    }  
    if( !img->imageData ) 
		   exit(-1);
	//IplImage *dst_image = cvCreateImage(cvGetSize(img),IPL_DEPTH_8U,3);
	//IplImage *src_image_32 = cvCreateImage(cvGetSize(img),IPL_DEPTH_8U,3);	   

	//cvConvertScale(src,src_image_32,1.0/255.0,0);//将原图RGB归一化到0-1之间
	//cvCvtColor(src_image_32,dst_image,CV_BGR2HSV);//得到HSV图

    IplImage* img1 = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
    cvCvtColor(img,img1,CV_BGR2GRAY);

    return img1;  
}
IplImage * resize(const IplImage * src, float t)
{
    IplImage *dst = 0;			
    float scale = t;		
    CvSize dst_cvsize;			
    dst_cvsize.width = src->width * scale;		
    dst_cvsize.height = src->height * scale;	
    dst = cvCreateImage( dst_cvsize, src->depth, src->nChannels);	
    cvResize(src, dst, CV_INTER_LINEAR);	
    return dst;
}
IplImage* color_country(IplImage* src)
{
	IplImage* img=0;
	int height,width,step,channels;
	char* data;
	int i,j,k;

	img=src;
	if(!img)
	{
	  printf("Could not load image file:%s\n");
	  exit(0);
	}
	height=img->height;
	width=img->width;
	step=img->widthStep;
	channels=img->nChannels;
	data=(char*)img->imageData;
	for(i=0;i<height;i++)
		 for(j=0;j<width;j++)
			 for(k=0;k<channels;k++)
				data[i*step+j*channels+k]=255-data[i*step+j*channels+k];

	return img;
}


int main(int argc, char** argv)
{
	//Load source image	

	const char* tmp=argv[1];
	IplImage* src_final= cvLoadImage(tmp);
	IplImage* temp=NULL;
	src_final=resize(src_final,0.32);
	Mat src1 = imread(tmp);

	IplImage imgIpl3 = (IplImage)(src1); 
	temp=&imgIpl3;
	temp=resize(temp,0.32);
	//resize(src,src, cv::Size(150, 150), (0, 0), (0, 0), 3);
	//IplImage* src_final= NULL;
	
	cv::Mat src;
	src =cv::cvarrToMat(temp);//opencv3.0 以上函数版本变化
	
	
	//imshow("src", src);
	IplImage* pImg = NULL; 
	IplImage* pContourImg = NULL;
	CvMemStorage * storage = cvCreateMemStorage(1024);
	CvSeq * contour = 0;
	
	
	//1. Remove Shadows
	//Convert to HSV
	Mat hsvImg;
	cvtColor(src, hsvImg, CV_BGR2HSV);
	Mat channel[3];
	split(hsvImg, channel);
	channel[2] = Mat(hsvImg.rows, hsvImg.cols, CV_8UC1, 200);//Set V 200
	//Merge channels
	merge(channel, 3, hsvImg);
	Mat rgbImg;
	cvtColor(hsvImg, rgbImg, CV_HSV2BGR);
	//imshow("1. \"Remove Shadows\"", rgbImg);

	//2. Convert to gray and normalize
	Mat gray(rgbImg.rows, src.cols, CV_8UC1);
	cvtColor(rgbImg, gray, CV_BGR2GRAY);
	normalize(gray, gray, 0, 255, NORM_MINMAX, CV_8UC1);
	//imshow("2. Grayscale", gray);

	//Mat tmp_Mat=cvarrToMat(gray);
	Mat element = getStructuringElement(cv::MORPH_ELLIPSE , Size(7, 7));
	morphologyEx(gray, gray,cv::MORPH_ELLIPSE , element);
	  
	GaussianBlur( gray, gray, Size( 3, 3 ), 0, 0 );//高斯模糊
	//*pImg = IplImage(tmp_Mat);

	//imshow("GaussianBlur", gray);

	IplImage img = gray;
	
	
	cvSmooth(&img, &img,  CV_GAUSSIAN  , 3, 0, 0, 0);
	
	//cvShowImage( "grey", &img );
	 
	//高斯滤波CV_GAUSSIAN
	cvSmooth(&img, &img,  CV_GAUSSIAN  , 3, 0, 0, 0);

	cvThreshold( &img, &img ,100, 255, CV_THRESH_BINARY ); //取阀值把图像转为二值图255
	//cvAdaptiveThreshold( &img, &img ,CV_THRESH_BINARY, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY );//自适应
	pImg=color_country(&img);
	//canny边缘检测
	cvSmooth(&img, &img,   CV_GAUSSIAN  , 3, 0, 0, 0);
	//cvShowImage( "result", pImg );

	IplImage* pCannyImg = NULL;
	pCannyImg = cvCreateImage(cvGetSize(pImg),
					  IPL_DEPTH_8U,
					  1);
	cvCanny(pImg, pCannyImg, 50, 150, 3);
	//cvShowImage( "canny", pCannyImg );
    pContourImg = cvCreateImage(cvGetSize(pImg),
					  IPL_DEPTH_8U,
					  3);
 	cvFindContours( pCannyImg, storage, &contour, sizeof(CvContour),CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

	//外轮廓
	cvDrawContours(pContourImg, contour, CV_RGB(0,0,255), CV_RGB(255, 0, 0),  2, 2, 8);
	//内轮廓
	//cvDrawContours(pContourImg, contour, CV_RGB(0,0,255), CV_RGB(255, 0, 0), -1, 2, 8);

	cvAddWeighted( pContourImg,0.5, src_final,0.5, 0.0,src_final);
	//去除椒盐噪声
	
	Mat medianimage;  
	cv::Mat src2;
	src2 =cv::cvarrToMat(src_final);
	medianBlur(src2,medianimage,5); 
	imshow("contour", medianimage);

	
	waitKey(0);
}

Mat sobel(Mat gray){
	Mat edges;

	int scale = 1;
	int delta = 0;
	int ddepth = CV_16S;
	Mat edges_x, edges_y;
	Mat abs_edges_x, abs_edges_y;
	Sobel(gray, edges_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
	convertScaleAbs( edges_x, abs_edges_x );
	Sobel(gray, edges_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
	convertScaleAbs(edges_y, abs_edges_y);
	addWeighted(abs_edges_x, 0.5, abs_edges_y, 0.5, 0, edges);

	return edges;
}

Mat canny(Mat src)
{
	Mat detected_edges;

	int edgeThresh = 1;
	int lowThreshold = 250;
	int highThreshold = 750;
	int kernel_size = 5;
	Canny(src, detected_edges, lowThreshold, highThreshold, kernel_size);

	return detected_edges;
}

Mat salt(const cv::Mat &image,int n){  //加噪
  
    Mat result = image.clone();  
    for (int i = 0; i<n;++i)  
    {  
        int row = rand()%result.rows;  
        int col = rand()%result.cols;  
        result.at<Vec3b>(row,col)[0] = 255;  
        result.at<Vec3b>(row,col)[1] = 255;  
        result.at<Vec3b>(row,col)[2] = 255;  
  
    }  
    return result;  
}  