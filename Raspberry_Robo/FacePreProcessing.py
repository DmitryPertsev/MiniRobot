import cv2
import os
import numpy as np

def adjust_gamma(image, gamma=1.0):

   invGamma = 1.0 / gamma
   table = np.array([((i / 255.0) ** invGamma) * 255
      for i in np.arange(0, 256)]).astype("uint8")

   return cv2.LUT(image, table)

def histeq(im,nbr_bins=256):
   #get image histogram
   imhist,bins = np.histogram(im.flatten(),nbr_bins,normed=True)
   cdf = imhist.cumsum() #cumulative distribution function
   cdf = 255 * cdf / cdf[-1] #normalize
   im2 = np.interp(im.flatten(),bins[:-1],cdf)

   return im2.reshape(im.shape), cdf

def PreProcessing_Origin(im, w, h):   
    max_brightness = 0.7
    min_brightness = 0.3
    brightness = np.sum(im) / (255 * w * h)
    ratio = brightness / max_brightness
    if ratio >= 1:
         print("Image bright enough")
         im = cv2.adjust_gamma(im,1/ratio)
    ration = brightness / min_brightness
    if ratio < 1:
         print("Image not bright enough")
         im = cv2.adjust_gamma(im,1/ratio)

    vertSeg = h//2
    horSeg = w//2
    
    X1 = im[0:vertSeg ,0:horSeg]
    X2 = im[0:vertSeg ,horSeg:w]
    X3 = im[vertSeg:h ,0:horSeg]
    X4 = im[vertSeg:h ,horSeg:w]

    X1 = np.array(X1, dtype = np.uint8)
    X2 = np.array(X2, dtype = np.uint8)
    X3 = np.array(X3, dtype = np.uint8)
    X4 = np.array(X4, dtype = np.uint8)

    X1 = histeq(X1)[0]
    X2 = histeq(X2)[0]
    X3 = histeq(X3)[0]
    X4 = histeq(X4)[0]

    up = np.concatenate((X1, X2), axis=1)
    down = np.concatenate((X3, X4), axis=1)
    X = np.concatenate((up, down), axis=0)
    Y2 = np.array(X, dtype = np.float64)
    Y3 = Y2
    for i in range(1,h-2):
       for j in  range(1, w-2):
            summ = (Y2[i,j] + Y2[i,j+1] + Y2[i,j-1] + Y2[i+1,j] + Y2[i-1,j] + Y2[i+1,j+1] + Y2[i+1,j-1] + Y2[i-1,j+1] + Y2[i-1,j-1])
            Y3[i,j] = summ/9 

    Y3 = ((Y3 - Y3.min()) / (Y3.max()-Y3.min())) * 255
    
    data = Y3.round().astype(np.uint8)

    data = unsharp_mask(data)
    
    return data


def unsharp_mask(image, kernel_size=(5, 5), sigma=1.0, amount=1.0, threshold=0):
    blurred = cv2.GaussianBlur(image, kernel_size, sigma)
    sharpened = float(amount + 1) * image - float(amount) * blurred
    sharpened = np.maximum(sharpened, np.zeros(sharpened.shape))
    sharpened = np.minimum(sharpened, 255 * np.ones(sharpened.shape))
    sharpened = sharpened.round().astype(np.uint8)
    if threshold > 0:
        low_contrast_mask = np.absolute(image - blurred) < threshold
        np.copyto(sharpened, image, where=low_contrast_mask)
    return sharpened

def PreProcessing_Basic(im, w, h):

    X1 = im[0:h ,0:w//2]
    X2 = im[0:h ,w//2:w]
    
    X1 = cv2.equalizeHist(X1)
    X2 = cv2.equalizeHist(X2)

    X1 = np.array(X1, dtype = np.uint8)
    X2 = np.array(X2, dtype = np.uint8)
    X = np.concatenate((X1, X2), axis=1)
    
    Y2 = np.array(X, dtype = np.float64)
    Y3 = Y2
    
    for i in range(1,h):
       for j in  range(1, w):
            Y3[i,j] = (Y2[i,j+1] + Y2[i,j-1] + Y2[i+1,j] + Y2[i-1,j] + Y2[i+1,j+1] + Y2[i+1,j-1] + Y2[i-1,j+1] + Y2[i-1,j-1])/8 
    
    data = Y3.astype(np.uint8)

    return data
