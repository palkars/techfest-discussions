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
