public class Planet {

    private static final double G = 6.67e-11;

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance (Planet p2) {
        double dx = p2.xxPos - this.xxPos;
        double dy = p2.yyPos - this.yyPos;

        return Math.pow(dx*dx + dy*dy, 0.5);
    }

    public double calcForceExertedBy (Planet p2) {
        double distance = calcDistance(p2);
        return G * this.mass * p2.mass / (distance * distance);
    }

    public double calcForceExertedByX (Planet p2) {
        double dx = p2.xxPos - this.xxPos;
        return calcForceExertedBy(p2) * dx / calcDistance(p2);
    }

    public double calcForceExertedByY (Planet p2) {
        double dy = p2.yyPos - this.yyPos;
        return calcForceExertedBy(p2) * dy / calcDistance(p2);
    }

    public double calcNetForceExertedByX (Planet[] planets) {

        double sum = 0;
        for (Planet planet : planets) {
            if (!this.equals(planet)) {
                sum += calcForceExertedByX(planet);
            }
        }

        return sum;

    }

    public double calcNetForceExertedByY (Planet[] planets) {
        double sum = 0;
        for (Planet planet : planets) {
            if (!this.equals(planet)) {
                sum += calcForceExertedByY(planet);
            }
        }

        return sum;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / this.mass;
        double aY = fY / this.mass;

        this.xxVel += dt * aX;
        this.yyVel += dt * aY;

        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;

    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
}
