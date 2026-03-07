# Exploring AI Models 7th March 2026

## What are tokens?
1. Tokens are the fundamental, small units of text (words, parts of words, or characters) that AI models use to process, understand, and generate language, acting as the "currency" or building blocks of AI interaction.
2. Through a process called tokenization, text is broken down into these manageable pieces to calculate probabilities for, and create, coherent responses. 
### Key Aspects of AI Tokens:
1. Definition: A token can be a whole word ("apple"), a part of a word ("un-", "believable"), or even a single character.
2. Tokenization Rule of Thumb: Generally, 1 token
3. 4 characters or 0.75 words in English.
4. Context Window: AI models have a maximum "context window" (token limit) representing the maximum total tokens they can read and write in a single interaction.
5. Function: They convert human language into numerical, manageable data points that models analyze to predict the next logical word in a sequence.
6. Usage Costs: AI services typically charge based on the total number of input and output tokens, making them the primary unit for calculating API costs. 
7. Tokens allow AI to manage memory constraints—if a conversation exceeds the token limit, older information is forgotten or "dropped

## Links
1. OpenAI - https://help.openai.com/en/articles/4936856-what-are-tokens-and-how-to-count-them
2. OpenAI https://ollama.com/library/mistral-small3.1
3. OepnAI TOkenizer https://platform.openai.com/tokenizer
4. NVIDIA article: https://blogs.nvidia.com/blog/ai-tokens-explained/

## Trying Mistal model
1. We downloaded mistral model as mentioned here:  https://ollama.com/library/mistral-small3.1
  1. ollama run mistral-small3.1
  2. We also ran claude with mistral-small3.1 model using:
  3. installed claude: https://code.claude.com/docs/en/quickstart#winget
     1. winget install Anthropic.ClaudeCode
  4. We launched claude with the model
    1. ollama launch claude --model mistral-small3.1
    2.  We asked it a few questions and how to train the model
      1. Prepare Your Data: Ensure your data is clean and in the correct format. This might involve data cleaning,
      normalization, and splitting your data into training, validation, and test sets.
      2. Set Up Your Environment: Make sure you have the necessary software and libraries installed. This typically includes
       a framework like PyTorch or TensorFlow, and any other dependencies the model requires.
      3. Load the Model: If Mistral-small3.1 is available as a pre-trained model, you can load it using the appropriate
      library. For example, if it's available in the Hugging Face Transformers library, you can load it using from
      transformers import AutoModelForSequenceClassification.
      4. Fine-Tune the Model: Use your prepared data to fine-tune the model. This usually involves setting up a training
      loop where the model learns from your data.
      5. Evaluate the Model: After training, evaluate the model's performance on your test set to ensure it generalizes well
       to new data.
      6. Save the Model: Once you're satisfied with the model's performance, save it so you can use it for inference later.
    

