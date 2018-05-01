# IP2ASN2CC

This Java library primarily provides a way to check if an **IP** address or **ASN** (Autonomous System Number) is 
formally registered under a **CC** (Country Code).

It means that you can check, for example, if the IP address `8.8.8.8` is formally registered to the country code "US" (United States).

Amongst other reasons, this is useful for applications that require to apply IP/ASN filtering to access resources -- e.g., blocking users from certain countries.

## How does it work

The library parses all the [RIR (Regional Internet Registry)](https://en.wikipedia.org/wiki/Regional_Internet_registry) databases, which 
follows the [RIR statistics exchange format](https://www.apnic.net/about-apnic/corporate-documents/documents/resource-guidelines/rir-statistics-exchange-format/). 
Therefore, it ensures that if an IP or ASN is published by a RIR (e.g., APNIC, ), then the IP2ASN2CC is able to find and correctly match IP/ASN to the respective country code.

# License

```

   Copyright 2015 AxLabs GmbH
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
       
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
```