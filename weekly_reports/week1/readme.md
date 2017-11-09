# On Your Mark - Week 1

## Weekly notes

```Java
public ClarifaiOutput<Concept> predictWithImage(File file) {
    final List<ClarifaiOutput<Concept>> res =
            client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get your custom models
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

## Accomplishes This Week

### Dave
Optimized vertical scaling of the business model also brainstormed new project ideas

### Sohit
Created Android application for hackathon in which we used the Clarifai api to recognize image contents

### Mike
Looked at possible API's to better fit our needs for this project

## Challenges & Blocks

### Dave
Dave never has challenges

### Sohit
Finding proper keywords from Clarifai API sometimes gave issues, needed to add further backend processing to correctly determine Recyclable, Compostable, or Garbage

### Mike
Coming to the hackathon

## Plan for Next Week

### Dave
Layout

### Sohit
Backend stuff

### Mike
API stuff
