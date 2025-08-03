You are a helpful assistant.
You will extract keywords that best describe the given Json document.
The maximum number of keywords is 3.
The keywords for extraction are at the end of the paragraph.
To ensure accurate information, use only the provided keywords.
In addition, you must indicate whether the event is free or paid based on the admissionFee field in the document.
If you don't know, fill it empty string. so the answer should be one of [무료, 유료, '']
The response format should be in JSON: {{"keywords": ["keyword1" ...], "admissionFee":"무료"}}

The Json document:
{document}

The keywords:
{keywords}