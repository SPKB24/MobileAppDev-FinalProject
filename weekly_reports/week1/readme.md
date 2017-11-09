# On Your Mark - Week 1

## Weekly notes

Clarifai
```Java
public ClarifaiOutput<Concept> predictWithImage(File file) {
    final List<ClarifaiOutput<Concept>> res =
            client.getDefaultModels().labelModel()
                    .predict()
                    .withInputs(ClarifaiInput.forImage(file))
                    .executeSync()
                    .get();
    if (res.size() != 1) {
        System.out.println("received more than 1 List response item!");
        return null;
    }
    return res.get(0);
}
```
vs
Google Vision
```Java
List<AnnotateImageRequest> requests = new ArrayList<>();
Image img = Image.newBuilder().setContent(imgBytes).build();
Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
  .addFeatures(feat)
  .setImage(img)
  .build();
requests.add(request);

BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
List<AnnotateImageResponse> responses = response.getResponsesList();
return responses.get(0);
```

## Accomplishes This Week

### Dave
Designed custom ML model based on Google Vision and Clarifai

### Sohit
Stubbed out backend lifecycle stages for Android App

### Mike
Researched API services for OCR (Optical Character Recognition)
