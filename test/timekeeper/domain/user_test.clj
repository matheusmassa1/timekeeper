(ns timekeeper.domain.user-test
  (:require [timekeeper.domain.user :refer :all]
            [clojure.test :refer :all])
  (:import [org.bson.types ObjectId]))

(defn mock-find-fn [user-info]
  (when (= (:username user-info) "existing-user")
    {:username "existing-user" :email "existing@example.com"}))

(defn mock-register-fn [user]
  user)

(deftest validate-user-registration-test
  (testing "Valid user registration data"
    (let [valid-data {:username "testuser" :email "test@example.com" :password "password123"}]
      (is (= valid-data (validate-user-registration valid-data)))))

  (testing "Invalid user registration data"
    (let [invalid-data {:username "testuser" :email "invalid-email"}]
      (is (nil? (validate-user-registration invalid-data))))))

(deftest validate-user-login-test
  (testing "Valid user login data"
    (let [valid-data {:username "testuser" :password "password123"}]
      (is (= valid-data (validate-user-login valid-data)))))

  (testing "Invalid user login data"
    (let [invalid-data {:username "testuser"}]
      (is (nil? (validate-user-login invalid-data))))))

(deftest extract-user-info-test
  (testing "Extract both username and email"
    (is (= [{:username "testuser"} {:email "test@example.com"}]
           (extract-user-info {:username "testuser" :email "test@example.com" :password "password123"}))))

  (testing "Extract only username"
    (is (= [{:username "testuser"}]
           (extract-user-info {:username "testuser" :password "password123"}))))

  (testing "Extract only email"
    (is (= [{:email "test@example.com"}]
           (extract-user-info {:email "test@example.com" :password "password123"}))))

  (testing "Extract nothing"
    (is (empty? (extract-user-info {:password "password123"})))))

(deftest user-exists?-test
  (testing "Existing user"
    (is (true? (user-exists? mock-find-fn {:username "existing-user"}))))

  (testing "Non-existing user"
    (is (nil? (user-exists? mock-find-fn {:username "new-user"})))))

(deftest register-user-test
  (testing "Register new user"
    (let [new-user {:username "new-user" :email "new@example.com" :password "password123"}
          result (register-user mock-find-fn mock-register-fn new-user)]
      (is (map? result))
      (is (= (:username result) "new-user"))
      (is (= (:email result) "new@example.com"))
      (is (instance? ObjectId (:_id result)))))

  (testing "Attempt to register existing user"
    (let [existing-user {:username "existing-user" :email "existing@example.com" :password "password123"}]
      (is (nil? (register-user mock-find-fn mock-register-fn existing-user)))))

  (testing "Attempt to register with invalid data"
    (let [invalid-user {:username "invalid-user"}]
      (is (nil? (register-user mock-find-fn mock-register-fn invalid-user))))))
