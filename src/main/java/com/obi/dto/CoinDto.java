package com.obi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CoinDto {
    private String id;
    private String name;
    private String symbol;
    private String image;
    private double currentPrice;
    private double marketCap;
    private double marketCapRank;
    private double totalVolume;
    private double high24h;
    private double low24h;
    private double priceChange24h;
    private double priceChangePercentage24h;
    private double marketCapChange24h;
    private double marketCapChangePercentage24h;
    private double circulatingSupply;
    private double totalSupply;
    private long ath;
    private long authChangePercentage;
    private Date altDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(double marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public double getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(double marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public double getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(double marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public long getAth() {
        return ath;
    }

    public void setAth(long ath) {
        this.ath = ath;
    }

    public long getAuthChangePercentage() {
        return authChangePercentage;
    }

    public void setAuthChangePercentage(long authChangePercentage) {
        this.authChangePercentage = authChangePercentage;
    }

    public Date getAltDate() {
        return altDate;
    }

    public void setAltDate(Date altDate) {
        this.altDate = altDate;
    }

    public long getAltChangePercentage() {
        return altChangePercentage;
    }

    public void setAltChangePercentage(long altChangePercentage) {
        this.altChangePercentage = altChangePercentage;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private long altChangePercentage;
    private Date lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
