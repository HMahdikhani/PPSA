Achieving Privacy-Preserving Subset Aggregation in Fog-Enhanced IoT

This is a simple implementation in Java that reports the execution time of each step including:

  step#1. submitting an encrypted query comprising attribute vector, threshold value, and random contamination value.
  step#3. IoT end-device response time.
  step#4. Fog node FN2 generating E(U.Vi-t)||E(D'i).
  step#5. Fog node FN1 generating Ci=E(D'i)^ci.
  step#6. Fog node FN2 aggregate Cis.

Fog-enhanced IoT (Internet of Things) is a fast-growing technology in which many firms and industries are currently investing to develop their own real-time and low latency scenarios. Compared with the traditional IoT, fog-enhanced IoT can offer a higher level of efficiency and stronger security by providing local data pre-processing, filtering, and forwarding mechanisms. However, fog-enhanced IoT faces some security and privacy challenges, since fog nodes are deployed at the network edge and may not be fully trustable. In this paper, we present a new privacy-preserving subset aggregation scheme, called PPSA, in fog-enhanced IoT scenarios, that enables a query user to gain the sum of data from a subset of IoT devices. To identify the subset, inner product similarity of the normalized vectors in the query user side and each IoT device is securely computed. If the inner product is greater than the user's specified threshold, IoT device's data will be privately aggregated to form the final response. To successfully launch privacy-preserving subset aggregation in the proposed scheme, we employ the Paillier homomorphic encryption to encrypt user's attribute vector, similarity threshold, IoT end-devices' data, as well as the intermediate results. To the best of our knowledge, this work is the first one to address the privacy-preserving subset aggregation in fog-enhanced IoT. We analyze and extensively evaluate the efficiency and security of the proposed PPSA scheme, and the detailed analysis and results indicate that our proposed PPSA scheme can practically achieve privacy-preserving subset aggregation with significant communication and computational cost saving.'


BibTex:
@article{mahdikhani2019achieving,
  title={Achieving Privacy-Preserving Subset Aggregation in Fog-Enhanced IoT},
  author={Mahdikhani, Hassan and Mahdavifar, Samaneh and Lu, Rongxing and Zhu, Hui and Ghorbani, Ali A},
  journal={IEEE Access},
  volume={7},
  pages={184438--184447},
  year={2019},
  publisher={IEEE}
}
