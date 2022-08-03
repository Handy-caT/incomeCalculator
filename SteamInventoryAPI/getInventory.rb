require 'httparty'
require 'json'

response = HTTParty.get('https://steamcommunity.com/id/TheHandiestCat/inventory/json/730/2')
json_response = JSON.parse(response.body)

json_count = json_response['rgInventory']
amount_hash = Hash.new(0)
json_count.each_key do |item|
  amount_hash[json_count[item]['classid']] += 1
end
puts(amount_hash)

name_hash = Hash.new(0)
json_response['rgDescriptions'].each_key do |key|
  name_hash[json_response['rgDescriptions'][key]['classid']] = json_response['rgDescriptions'][key]['market_hash_name']
end
puts(name_hash)

csv = CSV.open('inventory.csv', 'w')
csv << %w[ClassID Name Amount Volume RUB USD]

i = 0
amount_hash.each_key do |key|
  rub_price_response = HTTParty.get('https://steamcommunity.com/market/priceoverview/',
                                    query: { appid: 730, currency: 5, market_hash_name: name_hash[key] })
  usd_price_response = HTTParty.get('https://steamcommunity.com/market/priceoverview/',
                                    query: { appid: 730, currency: 1, market_hash_name: name_hash[key] })
  i += 2
  rub_price_json = JSON.parse(rub_price_response.body)
  usd_price_json = JSON.parse(usd_price_response.body)

  csv << [key, name_hash[key], amount_hash[key], rub_price_json['volume'], rub_price_json['lowest_price'],
          usd_price_json['lowest_price']]

  puts("#{i}  #{rub_price_json['lowest_price']}  #{name_hash[key]}")
  sleep(60) if (i % 10).zero?
end
