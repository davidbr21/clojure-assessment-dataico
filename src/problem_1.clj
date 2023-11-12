(ns problem_1)

; Reads invoice.edn file
(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

; Function that access to :retention/rate and compares its value, which must be of 1
(defn validate-retentions [item]
  (= 1 (get-in item [:retentionable/retentions 0 :retention/rate]))
  )

; Function that access to :tax/rate and compares its value, which must be of 19
(defn validate-taxes [item]
  (= 19 (get-in item [:taxable/taxes 0 :tax/rate]))
  )

; Function that filters items by tax rate equals than 19% and retention rate equals than 1%
; NOTE: In case you just want to validate the returned items ids, uncomment line 20.
(defn filter-invoices [] (->> (get invoice :invoice/items)
              (filter #(not(and (validate-retentions %) (validate-taxes %))))
              (filter #(or (validate-retentions %) (validate-taxes %)))
              ;(map :invoice-item/id)
              ))

(println (filter-invoices))