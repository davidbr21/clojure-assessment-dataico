(ns problem_2
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [clojure.data.json :as json]
            [invoice-spec :as is]
            ))

(import java.text.SimpleDateFormat)
(import java.util.Date)

; Function that reads from a JSON file and returns a string containing the JSON read
(defn read-json-from-file [file-path]
  (with-open [reader (clojure.java.io/reader file-path)]
    (json/read-str (apply str (line-seq reader)))))

; Returns invoice and converts object keys from string to keywords
(defn get-keywordized-invoice [file-path] (walk/keywordize-keys (read-json-from-file file-path)))
(def unformatted-invoice (get (get-keywordized-invoice "invoice.json") :invoice))   ; Access to :invoice value

; Keys to be replaced from invoice's first value, where the fist value is the initial keyword and the second is the new one.
(def key-replacements {
                       :customer :invoice/customer
                       :issue_date :invoice/issue-date
                       :items :invoice/items
                       :price :invoice-item/price
                       :quantity :invoice-item/quantity
                       :sku :invoice-item/sku
                       :taxes :invoice-item/taxes
                       :company_name :customer/name
                       :email :customer/email
                       :tax_rate :tax/rate
                       :tax_category :tax/category
                       })

; Function to parse a string with format "dd/MM/yyyy" into a date
(defn parse-date [date-str]
  (let [date-format (SimpleDateFormat. "dd/MM/yyyy")]
    (.parse date-format date-str)))

; Maps :taxes format, converting rate to double and category to :iva
(defn update-taxes-format [item]
  (update item :invoice-item/taxes #(mapv (fn [tax] (-> tax
                                                     (update :tax/rate (fn [rate] (double rate)))
                                                     (assoc :tax/category :iva)
                                                     ))
                                                %)))

; Giving the requested format to the invoice
(def invoice
  (-> unformatted-invoice
        (#(walk/postwalk-replace key-replacements %))
        (update :invoice/issue-date #(parse-date %))
        (update :invoice/items #(mapv update-taxes-format %))
       ))

; Validating ::is/invoice test cases
(println (s/valid? ::is/invoice invoice))